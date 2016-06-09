(ns followr.config
  (:require [nomad :refer [defconfig]]
            [clojure.java.io :as io]
            [clj-time.core :as t :refer [days]]))

(defconfig config (io/resource "config.edn"))

(defn follow-limit []
  (let [follow-limit (:follow-limit (config))]
    (cond
      (string? follow-limit) (Integer/parseInt follow-limit)
      (number? follow-limit) (int follow-limit)
      (nil? follow-limit) 5)))

(defn follow-duration []
  (let [follow-duration-days (:follow-duration-days (config))]
    (-> (cond
          (string? follow-duration-days) (Integer/parseInt follow-duration-days)
          (number? follow-duration-days) (int follow-duration-days)
          (nil? follow-duration-days) 14)
        days)))
