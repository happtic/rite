(defproject rite "0.1.0-SNAPSHOT"
  :description "rite - String Similarity"
  :url "https://hamishrickerby.com/"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.758"]
                 [camel-snake-kebab "0.4.1"]]
  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-doo "0.1.10"]]

  :doo {:paths {:karma "node_modules/karma/bin/karma"}}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :compiler {
                           :main rite.core
                           :output-to "target/rite.js"
                           :output-dir "target"
                           :optimizations :none
                           :cache-analysis true
                           :source-map true}}
               {:id "browser-test"
                :source-paths ["src" "test"]
                :compiler {:output-to "target/browser_tests.js"
                           :main rite.browser
                           :optimizations :none}}]})
