(ns authorizer.controllers.transaction-test
  (:require [authorizer.controllers.transaction :refer [create-transaction!]]
            [clojure.test :refer [deftest testing is]]
            [authorizer.ports.storage :refer [create-in-memory-storage]]
            [authorizer.db.transaction :as transaction-db]
            [authorizer.db.account :as account-db]
            [authorizer.adapter :as adapter]))

(deftest transaction-with-valid-conditions
  (testing "should create transaction "
    (let [storage (create-in-memory-storage)
          input "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          created-transaction {:merchant "Sorveteria Supimpa", :amount 20 :time "2019-02-13T11:00:00.000Z"}
          created-account-with-new-amount {:active-card true, :available-limit 80}]

      (account-db/create! storage {:account {:active-card true, :available-limit 100}})
      (create-transaction! input storage)

      (is (= (first (transaction-db/select-all storage)) created-transaction))
      (is (= (account-db/select-all storage) created-account-with-new-amount)))))

(deftest transaction-with-account-not-initialized
  (testing "should validate transaction with acccount not initialized"
    (let [storage (create-in-memory-storage)
          input "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          account-not-initialized-violation {:account {} :violations ["account-not-initialized"]}]

      (is (= (transaction-db/select-all storage) nil))
      (is (= (create-transaction! input storage) (adapter/hmap-to-json account-not-initialized-violation))))))

(deftest transaction-with-card-inactive
  (testing "should validate transaction with inactive card"
    (let [storage (create-in-memory-storage)
          input "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          card-inactive-violation {:account {:active-card false :available-limit 100} :violations ["card-inactive"]}]

      (account-db/create! storage {:account {:active-card false, :available-limit 100}})

      (is (= (transaction-db/select-all storage) nil))
      (is (= (create-transaction! input storage) (adapter/hmap-to-json card-inactive-violation))))))

(deftest transaction-with-high-frequency
  (testing "should validate transaction with high frequency conditions"
    (let [storage (create-in-memory-storage)
          input "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:02:00.000Z\"}}"
          sample1 "{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 30, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          sample2 "{\"transaction\": {\"merchant\": \"Burger Queen\", \"amount\": 40, \"time\": \"2019-02-13T11:01:00.000Z\"}}"
          sample3 "{\"transaction\": {\"merchant\": \"Fast Noodles\", \"amount\": 5, \"time\": \"2019-02-13T11:01:50.000Z\"}}"
          high-frequency-violation {:account {:active-card true :available-limit 25} :violations ["high-frequency-small-interval"]}]

      (account-db/create! storage {:account {:active-card true, :available-limit 100}})

      (create-transaction! sample1 storage)

      (create-transaction! sample2 storage)

      (create-transaction! sample3 storage)

      (is (= (create-transaction! input storage) (adapter/hmap-to-json high-frequency-violation)))
      (is (= (count (transaction-db/select-all storage)) 3)))))

(deftest transaction-doubled
  (testing "should validate doubled transactions"
    (let [storage (create-in-memory-storage)
          input "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          sample1 "{\"transaction\": {\"merchant\": \"Sorveteria Supimpa\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}"
          double-transaction {:account {:active-card true :available-limit 80} :violations ["double-transaction"]}]

      (account-db/create! storage {:account {:active-card true, :available-limit 100}})

      (create-transaction! sample1 storage)

      (is (= (create-transaction! input storage) (adapter/hmap-to-json double-transaction)))
      (is (= (count (transaction-db/select-all storage)) 1)))))
