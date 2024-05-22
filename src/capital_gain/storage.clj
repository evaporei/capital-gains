(ns capital-gain.storage)
 
(defprotocol Storage
  "Protocol for saving/retrieving data from a Storage."
  (set-key! [this new-key new-value] "Sets value in key of Storage.")
  (update-key! [this k update-fn] "Updates via custom fn a key's value of Storage.")
  (get-key [this k default] "Gets key of Storage.")
  (clear! [this] "Clears the storage so it's empty"))
