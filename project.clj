(defproject authorizer "0.1.0-SNAPSHOT"
  :description "Authorizer"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [prismatic/schema "1.1.12"]
                 [cheshire "5.8.0"]
                 [clj-time "0.15.2"]]

  :main authorizer.ports.stdin

  :repl-options {:init-ns authorizer.ports.stdin})
