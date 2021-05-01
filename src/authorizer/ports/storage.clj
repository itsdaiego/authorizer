(ns authorizer.ports.storage
  (:require [authorizer.protocols.storage-client :as storage-client]))

(defrecord Storage [storage]
  storage-client/StorageClient
  (get-all [_this] @storage)
  (create! [_this create-fn] (swap! storage create-fn)))

(defn create-in-memory-storage []
  (->Storage (atom {})))

