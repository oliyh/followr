(defproject followr "0.1.0-SNAPSHOT"
  :description "An experiment in asymmetrical Flickr behaviour"
  :url "https://github.com/oliyh/followr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [jarohen/nomad "0.7.2"]
                 [clj-http "2.0.1"]
                 [clj-oauth "1.5.5"]
                 [cheshire "5.6.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [clj-time "0.12.0"]

                 ;; persistence
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.postgresql/postgresql "9.4.1208"]
                 [com.h2database/h2 "1.4.192"]
                 [com.mchange/c3p0 "0.9.5.2"]
                 [joplin.core "0.3.6"]
                 [joplin.jdbc "0.3.6"]
                 [honeysql "0.7.0"]]
  :target-path "target/%s"
  :main ^:skip-aot followr.core
  :resource-paths ["resources" "migrators"]
  :min-lein-version "2.0.0"
  :uberjar-name "followr-standalone.jar"
  :profiles {:uberjar {:aot :all}})
