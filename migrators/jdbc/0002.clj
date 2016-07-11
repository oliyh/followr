(ns jdbc.0002
  (:require [clojure.java.jdbc :as sql]
            [joplin.jdbc.database]))

(defn up [db]
  (sql/with-db-connection [db db]
    (sql/db-do-commands
     db
     (sql/create-table-ddl :following_archive
                           [:id "bigserial primary key"]
                           [:user_ids "text"]))))

(defn down [db]
  (sql/with-db-connection [db db]
    (sql/db-do-commands
     db
     (sql/drop-table-ddl :following_archive))))
