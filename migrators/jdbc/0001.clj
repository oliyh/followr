(ns jdbc.0001
  (:require [clojure.java.jdbc :as sql]
            [joplin.jdbc.database]))

(defn up [db]
  (sql/with-db-connection [db db]
    (sql/db-do-commands
     db
     (sql/create-table-ddl :following
                           [:id "bigserial primary key"]
                           [:user_id "varchar(128)"]
                           [:followed_on "TIMESTAMP NOT NULL DEFAULT now()"])
     "CREATE UNIQUE INDEX following_user_id_idx ON following(user_id);"
     "CREATE INDEX following_followed_on_idx ON following(followed_on);")))

(defn down [db]
  (sql/with-db-connection [db db]
    (sql/db-do-commands
     db
     (sql/drop-table-ddl :following))))
