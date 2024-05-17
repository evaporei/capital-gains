(ns capital-gain.ports
  (:require [clojure.string :refer [includes?]]
            [capital-gain.controller :refer [controller]]
            [capital-gain.in-memory-storage :refer [new-in-memory-storage]]
            [capital-gain.storage :as storage]))

(defn cli-stdin!
  "Receives via stdin the user input line by line and
  executes the controller with it."
  []
  (let [storage (new-in-memory-storage)
        buffer (atom "")]
    (doseq [input-line (line-seq (java.io.BufferedReader. *in*))]
      (swap! buffer str input-line)
      (when (includes? @buffer "]")
          (->> @buffer
               (controller storage)
               print)
          (storage/clear! storage)
          (reset! buffer "")))))
