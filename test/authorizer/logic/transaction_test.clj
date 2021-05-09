(ns authorizer.logic.transaction-test
  (:require [authorizer.logic.transaction :refer [has-sufficient-funds? has-high-frequency-transactions? has-doubled-transactions?]]
            [clojure.test :refer [deftest testing is]]))

(deftest limit-is-greater-for-transaction
  (testing "should return true if current limit is greater than curent transaction's amount"
    (let [account {:active-card false, :available-limit 100}
          transaction {:merchant "Burger King", :amount 50, :time "2021-02-13T11:00:00.000Z"}
          expected true]
      (is (= (has-sufficient-funds? account transaction) expected)))))

(deftest limit-is-equal-for-transaction
  (testing "should return true if current limit is equal than curent transaction's amount"
    (let [account {:active-card false, :available-limit 100}
          transaction {:merchant "Burger King", :amount 100, :time "2021-02-13T11:00:00.000Z"}
          expected true]
      (is (= (has-sufficient-funds? account transaction) expected)))))

(deftest limit-is-less-for-transaction
  (testing "should return false if current limit is less than curent transaction's amount"
    (let [account {:active-card false, :available-limit 100}
          transaction {:merchant "Burger King", :amount 101, :time "2021-02-13T11:00:00.000Z"}
          expected false]
      (is (= (has-sufficient-funds? account transaction) expected)))))

(deftest has-high-frequency-transactions
  (testing "should return true if there's more than three transactions create in under three minutes"
    (let [transactions '({:merchant "McNuggets", :amount 101, :time "2021-02-13T11:02:00.000Z"},
                         {:merchant "Temaki em ação", :amount 101, :time "2021-02-13T11:02:00.000Z"},
                         {:merchant "Sorveteria Supimpa", :amount 101, :time "2021-02-13T11:01:00.000Z"},
                         {:merchant "Burger King", :amount 101, :time "2021-02-13T11:00:00.000Z"})
          expected true]
      (is (= (has-high-frequency-transactions? transactions) expected)))))

(deftest has-not-high-frequency-transactions
  (testing "should return false if there's no more than three transactions create in under three minutes"
    (let [transactions '({:merchant "McNuggets", :amount 101, :time "2021-02-13T11:02:59.000Z"},
                         {:merchant "Temaki em ação", :amount 101, :time "2021-02-13T11:02:00.000Z"},
                         {:merchant "Sorveteria Supimpa", :amount 101, :time "2021-02-13T11:01:00.000Z"},
                         {:merchant "Burger King", :amount 101, :time "2021-02-13T11:00:00.000Z"}),
          expected false]
      (is (= (has-high-frequency-transactions? transactions) expected)))))

(deftest has-doubled-transactions
  (testing "should return true if there's more than one transaction create in under two minutes with the same amount and merchant"
    (let [transactions '({:merchant "Burger King", :amount 101, :time "2021-02-13T11:03:00.000Z"},
                         {:merchant "Burger Queen", :amount 99, :time "2021-02-13T11:03:00.000Z"},
                         {:merchant "Burger King", :amount 101, :time "2021-02-13T11:02:26.000Z"}),
          expected true]
      (is (= (has-doubled-transactions? transactions) expected)))))

(deftest has-not-doubled-transactions
  (testing "should return false if there's no more than one transaction create in under two minutes with the same amount and merchant"
    (let [transactions '({:merchant "McNuggets", :amount 101, :time "2021-02-13T11:03:27.000Z"}
                         {:merchant "McDonalds", :amount 99, :time "2021-02-13T11:03:00.000Z"},
                         {:merchant "McNuggets", :amount 100, :time "2021-02-13T11:02:26.000Z"})
          expected false]
      (is (= (has-doubled-transactions? transactions) expected)))))
