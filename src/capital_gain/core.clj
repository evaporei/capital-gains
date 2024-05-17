(ns capital-gain.core
  (:gen-class)
    (:require [capital-gain.ports :as ports]))

(defn -main
  "Starts the application via CLI with stdin."
  [& _]
  (ports/cli-stdin!))
