(ns followr.flickr
  (:require [followr.config :refer [config]]
            [clj-http.client :as http]
            [oauth.client :as oauth]))

(def flickr-rest-endpoint "https://api.flickr.com/services/rest")

(defonce consumer (delay
                   (let [{:keys [consumer-key consumer-secret]} (config)]
                     (oauth/make-consumer consumer-key
                                          consumer-secret
                                          "https://www.flickr.com/services/oauth/request_token"
                                          "https://www.flickr.com/services/oauth/access_token"
                                          "https://www.flickr.com/services/oauth/authorize"
                                          :hmac-sha1))))

;; doesn't work
#_(def request-token (oauth/request-token consumer "http://localhost/oauth"))
#_(oauth/user-approval-uri consumer (:oauth_token request-token))

(defn- credentials [op]
  (let [{:keys [token token-secret]} (config)]
    (oauth/credentials @consumer
                       token
                       token-secret
                       :POST
                       flickr-rest-endpoint
                       op)))

(defn call-flickr [op]
  (let [op (merge {:format "json"
                   :nojsoncallback 1} op)]
    (http/post flickr-rest-endpoint {:query-params (merge (credentials op) op)
                                     :as :json})))


(defn add-contact [user-id]
  (call-flickr {:method "flickr.contacts.add"
                :user_id user-id
                :family 0
                :friend 0}))

(defn list-contacts []
  (call-flickr {:method "flickr.contacts.getList"}))
