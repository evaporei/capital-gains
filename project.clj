(defproject capital-gain "0.1.0-SNAPSHOT"
  :description "nubank's capital gain code challenge"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.5.0"]]
  :main ^:skip-aot capital-gain.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
