(ns authorizer.ports.stdin
  (:require [authorizer.controllers.account :as account-controller]
            [authorizer.controllers.transaction :as transaction-controller]
            [authorizer.ports.storage :refer [create-in-memory-storage]]))


(defn perform-operation
  [payload storage]
  (cond 
    (:contains? payload :account) (account-controller/create-account payload storage)
    (:contains? payload :transaction) (transaction-controller/create-transaction)))

(defn -main 
  []
  (let [storage (create-in-memory-storage)]
    (doseq [payload (line-seq (java.io.BufferedReader. *in*))]
      (-> (perform-operation payload storage)
          (println)))))
