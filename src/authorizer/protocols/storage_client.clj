(ns authorizer.protocols.storage-client
  (:require [schema.core :as s]))

(defprotocol StorageClient
  (get-all [storage])
  (create! [storage payload]))

(def IStorageClient (s/protocol StorageClient))

