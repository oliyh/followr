(ns followr.config
  (:require [nomad :refer [defconfig]]
            [clojure.java.io :as io]))

(defn safe-parse-edn [x]
  (if (string? x)
    (nomad/parse-edn x)
    x))

(def data-readers
  {'followr/edn-env-var (comp safe-parse-edn nomad/read-env-var)})

(defconfig config (io/resource "config.edn") {:data-readers data-readers})
