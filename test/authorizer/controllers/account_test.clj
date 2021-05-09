(ns authorizer.controllers.account-test
  (:require [authorizer.controllers.account :refer [create-account]]
            [clojure.test :refer [deftest testing is]]
            [authorizer.ports.storage :refer [create-in-memory-storage]]
            [authorizer.db.account :as account-db]
            [authorizer.adapter :as adapter]))

(deftest create-valid-account
  (testing "should create valid account"
    (let [storage (create-in-memory-storage)
          input "{\"account\": {\"active-card\": true, \"available-limit\": 750}}"
          created-account {:active-card true, :available-limit 750}]
      (is (= (create-account  input storage) (adapter/hmap-to-json {:account created-account :violations []})))
      (is (= (account-db/select-all storage) created-account)))))

(deftest create-duplicated-account
  (testing "should fail to create duplicated account"
    (let [storage (create-in-memory-storage)
          input "{\"account\": {\"active-card\": true, \"available-limit\": 750}}"
          created-account {:active-card true, :available-limit 750}
          duplicated-account {:account {:active-card true, :available-limit 750}, :violations ["account-already-initialized"]}]

      (create-account  input storage)

      (is (= (create-account  input storage) (adapter/hmap-to-json duplicated-account)))
      (is (= (account-db/select-all storage) created-account)))))
