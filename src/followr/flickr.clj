(ns followr.flickr
  (:require [followr.config :refer [config]])
  (:import [com.flickr4java.flickr Flickr  REST]
           [com.flickr4java.flickr.auth Permission]
           [org.scribe.model Token Verifier]))

(defn- flickr-auth []
  (let [{:keys [flickr-key flickr-secret]} (config)
        flickr (Flickr. flickr-key flickr-secret (REST.))]
    (.getAuthInterface flickr)))

(defn request-auth []
  (let [flickr-auth (flickr-auth)
        request-token (.getRequestToken flickr-auth)]
    (println (format "Visit %s to obtain authorisation code"
                     (.getAuthorizationUrl flickr-auth request-token Permission/DELETE)))

    (fn obtain-access-token [auth-code]
      (let [access-token (.getAccessToken flickr-auth request-token (Verifier. auth-code))
            auth (.checkToken flickr-auth access-token)]
        {:request-token (.getToken access-token)
         :request-secret (.getSecret access-token)
         :nsid (.. auth getUser getId)
         :real-name (.. auth getUser getRealName)
         :user-name (.. auth getUser getUsername)
         :permission (.. auth getPermission getType)}))))
