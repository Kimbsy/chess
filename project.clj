(defproject chess "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot chess.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
