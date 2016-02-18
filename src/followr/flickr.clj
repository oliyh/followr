(ns followr.flickr
  (:require [followr.config :refer [config]]
            [clj-http.client :as http]
            [oauth.client :as oauth]
            [cheshire.core :as json]
            [clj-time.coerce :as tc]))

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
    (-> (http/post flickr-rest-endpoint {:query-params (merge (credentials op) op)
                                         :as :json})
        :body)))


(defn add-contact! [user-id]
  (call-flickr {:method "flickr.contacts.add"
                :user_id user-id
                :family 0
                :friend 0}))

(defn remove-contact! [user-id]
  (call-flickr {:method "flickr.contacts.remove"
                :user_id user-id}))

(defn list-contacts []
  (call-flickr {:method "flickr.contacts.getList"}))

(defn random-group-members [group-id]
  (let [{:keys [pages]} (:members (call-flickr {:method "flickr.groups.members.getList"
                                                :group_id group-id
                                                :per_page 0}))]
    (->> (call-flickr {:method "flickr.groups.members.getList"
                       :group_id group-id
                       :page (rand-int pages)})
         :members
         :member
         (map :nsid))))

(defn user-photos-summary [user-id]
  (println "Calling user summary")
  (let [response (->> (call-flickr {:method "flickr.people.getPublicPhotos"
                                    :user_id user-id
                                    :per_page 1
                                    :extras "date_upload"})
                      :photos)
        result
        {:user-id user-id
         :photo-count (Long/parseLong (:total response))
         :last-uploaded (some-> response :photo first :dateupload Long/parseLong (* 1000) tc/from-long)}]
    (println result)
    result))

(defn find-groups [q]
  (call-flickr {:method "flickr.groups.search"
                :text q}))
