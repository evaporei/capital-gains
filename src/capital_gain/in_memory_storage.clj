(ns capital-gain.in-memory-storage
  (:require [capital-gain.storage :refer [Storage]]))

(defrecord InMemoryStorage [storage]
  Storage
  (set-key! [_this new-key new-value]
    (swap! storage assoc new-key new-value))
  (update-key! [_this k update-fn]
    (swap! storage update-in [k] update-fn))
  (get-key [_this k default]
    (k @storage default))
  (clear! [_this]
    (reset! storage {})))

(defn new-in-memory-storage
  "Creates a new InMemoryStorage with an atom for concurrency safety."
  ([]
   (new-in-memory-storage {}))
  ([initial-storage]
   (->InMemoryStorage (atom initial-storage))))

