(defproject followr "0.1.0-SNAPSHOT"
  :description "An experiment in asymmetrical Flickr behaviour"
  :url "https://github.com/oliyh/followr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [jarohen/nomad "0.7.2"]
                 [clj-http "2.0.1"]
                 [com.flickr4java/flickr4java "2.15"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
