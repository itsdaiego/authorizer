(ns authorizer.db.account
  (:require [authorizer.protocols.storage-client :as storage-client]))

(defn create!
  [storage account]
  (storage-client/create! storage #(assoc % :account (:account account) :violations (:violations account))))

(defn select-all
  [storage]
  (:account (storage-client/get-all storage)))
