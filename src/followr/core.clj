(ns followr.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [followr.db :as db]
            [followr.config :refer [config]]
            [followr.flickr :as flickr]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

(defn- current-following [db]
  (map :user_id (jdbc/query db (sql/format {:select [:user_id]
                                            :from [:following]}))))

(defn mark-followed! [db user-id]
  (jdbc/with-db-transaction [db db]
    (jdbc/insert! db :following {:user_id user-id})))

(defn- find-candidates []
  (->> (flickr/list-group-members (rand-nth ["38436807@N00" ;;flickr today
                                             "34427469792@N01" ;; flickr central
                                             ]))
       (take 20)
       (shuffle)
       (take 2)))

(defn -main [& args]
  (let [{:keys [db-url]} (config)
        db (db/create-db-connection db-url)
        currently-following (set (current-following db))
        candidates (find-candidates)]

    (log/info "Currently following" (count currently-following))
    (log/info "Found" (count candidates) "candidates")

    (doseq [candidate candidates]
      (if (contains? currently-following candidate)
        (log/info "Already following" candidate)

        (do (log/info "Following" candidate)
            (mark-followed! db candidate)
            (flickr/add-contact! candidate))))))
