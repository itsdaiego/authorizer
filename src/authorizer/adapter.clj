(ns authorizer.adapter
  (:require [cheshire.core :refer [parse-string]]))

(defn json_to_map
  [input]
  (parse-string input true))
