(ns authorizer.logic.transaction
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

(defn has-sufficient-funds?
  [account transaction]
  (let [current-limit (:available-limit account)
        amount (:amount transaction)]
    (cond
      (>= (- (int current-limit) (int amount)) 0) true
      :else false)))

(defn has-high-frequency-transactions?
  [transactions]
  (cond
    (<= (count transactions) 3) false
    :else (let [current-transaction-time (:time (first transactions))
                fourth-transaction-time (:time (nth transactions 3))]
            (<= (t/in-seconds
                 (t/interval
                  (f/parse fourth-transaction-time)
                  (f/parse current-transaction-time))) 120))))

(defn has-doubled-transactions?
  [transactions]
  (cond
    (<= (count transactions) 1) false
    :else (let [current-transaction (first transactions)
                previous-transactions (drop 1 transactions)
                last-min-transactions (filter #(<= (t/in-seconds
                                                    (t/interval
                                                     (f/parse (:time %))
                                                     (f/parse (:time current-transaction)))) 60) previous-transactions)
                has-doubled-transactions (filter #(and
                                                   (= (:merchant current-transaction) (:merchant %))
                                                   (= (:amount current-transaction) (:amount %))) last-min-transactions)]
            (-> has-doubled-transactions
                (count)
                (zero?)
                (not)))))
