(ns authorizer.db.transaction
  (:require [authorizer.protocols.storage-client :as storage-client]))

(defn create!
  [storage transaction]
  (storage-client/create! storage #(update-in % [:transactions] conj transaction)))

(defn select-all
  [storage]
  (:transactions (storage-client/get-all storage)))
