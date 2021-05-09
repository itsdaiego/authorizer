(ns authorizer.ports.stdin
  (:require [authorizer.ports.storage :refer [create-in-memory-storage]]
            [authorizer.router :as router]))

(defn -main
  []
  (let [storage (create-in-memory-storage)]
    (doseq [payload (line-seq (java.io.BufferedReader. *in*))]
      (-> (router/forward-operation payload storage)
          (println)))))
