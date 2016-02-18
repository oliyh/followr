(ns followr.db
  (:require [clojure.java.jdbc :as jdbc]
            [joplin.core :as joplin]
            [joplin.jdbc.database])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource]))

(defn- ->jdbc-database-url [url]
  (condp re-find url
    #"^postgres://" ;; heroku
    (let [[_ user password host port database]
          (re-matches #"postgres://(.*):(.*)@(.*):(.*)/(.*)" url)]
      (format "jdbc:postgresql://%s:%s/%s?user=%s&password=%s"
              host port database user password))

    url))

(defn- migrate-db [url]
  (with-out-str
    (joplin/migrate-db
     {:db {:type :jdbc
           :url url}
      :migrator "migrators/jdbc"})))

(defn- wipe-db [url]
  (with-out-str
    (joplin/rollback-db
     {:db {:type :jdbc
           :url url}
      :migrator "migrators/jdbc"}
     Integer/MAX_VALUE)))

(defn- pool [driver url]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass driver)
               (.setJdbcUrl url)

               (.setMinPoolSize 1)
               (.setMaxPoolSize 8)
               (.setAcquireIncrement 1)
               (.setPreferredTestQuery "select 1")

               (.setTestConnectionOnCheckout true)
               (.setCheckoutTimeout 7000)
               (.setAcquireRetryAttempts 3)
               (.setAcquireRetryDelay 1000)

               ;; expire excess connections after 30 minutes of inactivity:
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity:
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(defn create-db-connection [url]
  (let [url (->jdbc-database-url url)]
    (migrate-db url)

    (condp re-find url
      #":postgresql:" (with-meta (pool "org.postgresql.Driver" url) {:type :postgres})
      #":h2:" (with-meta (pool "org.h2.Driver" url) {:type :h2})

      (throw (RuntimeException. "Don't know what driver to use with" url)))))

(defn create-fresh-db-connection [url]
  (let [url (->jdbc-database-url url)]
    (wipe-db url)
    (create-db-connection url)))
