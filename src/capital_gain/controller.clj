(ns capital-gain.controller
  (:require ;;[capital-gain.business-logic :as business-logic]
            [capital-gain.adapters :as adapters]
            ;;[capital-gain.database :as db]
            ))


;; (defn execute-controller
;;   "Executes controller with storage and user input."
;;   [storage controller-and-input]
;;   (let [[controller input-data] controller-and-input]
;;       (controller storage input-data)))

(defn controller
  "Adapts and routes the user input to the correct controller, and executes it.
  It returns the JSON string just as it received."
  [storage input]
  (->> input
       adapters/json->edn
       ;; routing
       ;; (execute-controller storage)
       adapters/edn->json))
