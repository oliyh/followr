(defproject followr "0.1.0-SNAPSHOT"
  :description "An experiment in asymmetrical Flickr behaviour"
  :url "https://github.com/oliyh/followr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [jarohen/nomad "0.7.2"]
                 [clj-http "2.0.1"]
                 [clj-oauth "1.5.4"]
                 [cheshire "5.5.0"]
                 [org.clojure/tools.logging "0.3.1"]

                 ;; persistence
                 [org.clojure/java.jdbc "0.3.5"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [com.h2database/h2 "1.4.189"]
                 [com.mchange/c3p0 "0.9.5.1"]
                 [joplin.core "0.2.12"]
                 [joplin.jdbc "0.2.12"]
                 [honeysql "0.6.1"]]
  :target-path "target/%s"
  :main ^:skip-aot followr.core
  :resource-paths ["resources" "migrators"]
  :profiles {:uberjar {:aot :all}})
