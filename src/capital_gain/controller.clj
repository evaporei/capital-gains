(ns capital-gain.controller
  (:require ;;[capital-gain.business-logic :as business-logic]
            [capital-gain.adapters :as adapters]
            [capital-gain.database :as db]))

(defn buy-stocks
  "Buy stocks"
  [storage trade]
  ;; (db/buy-stocks! storage trade)
  (print "buy")
  trade)

(defn sell-stocks
  "Sell stocks"
  [storage trade]
  (print "sell")
  trade)

;; review
(defn execute-controller
  "Executes controller with storage and user input."
  [storage controller-and-input]
  (let [[controller input-data] controller-and-input]
      (controller storage input-data)))

(defn routing
  "Routes to appropriate controller based of map key."
  [trade]
  (case (:operation trade)
    "buy" [buy-stocks trade]
    "sell" [sell-stocks trade]
    [(constantly nil) trade]))

(defn controller
  "Adapts and routes the user input to the correct controller, and executes it.
  It returns the JSON string just as it received."
  [storage input]
  (->> input
       adapters/json->edn
       ;; ewww
       (map (comp (partial execute-controller storage) routing))
       adapters/edn->json))
