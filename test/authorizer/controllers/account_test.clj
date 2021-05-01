(ns authorizer.controllers.account-test
  (:require [authorizer.controllers.account :refer [create-account]]
            [clojure.test :refer [deftest testing is]]
            [authorizer.ports.storage :refer [create-in-memory-storage]]
            [authorizer.protocols.storage-client :as storage-client]))

(def storage (create-in-memory-storage))

(deftest create-valid-account
  (testing "should create valid account"
    (let [input "{\"account\": {\"active-card\": true, \"available-limit\": 750}}"
          created-account {:account {:active-card true, :available-limit 750}, :violations []}]
      (is (= (create-account  input storage) created-account))
      (is (= (storage-client/get-all storage) created-account)))))

(deftest create-duplicated-account
  (testing "should fail to create duplicated account"
    (let [input "{\"account\": {\"active-card\": true, \"available-limit\": 750}}"
          created-account {:account {:active-card true, :available-limit 750}, :violations []}
          duplicated-account {:account {:active-card true, :available-limit 750}, :violations ["account-already-initialized"]}]
      (is (= (create-account  input storage) duplicated-account))
      (is (= (storage-client/get-all storage) created-account)))))
