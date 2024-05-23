(ns capital-gains.core
  (:gen-class)
  (:require [capital-gains.ports :as ports]))

(defn -main
  "Starts the application via CLI with stdin."
  [& _]
  (ports/cli-stdin!))
