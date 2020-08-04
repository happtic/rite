(ns rite.core-test
  (:require [rite.core :as sut]
            [cljs.test :refer-macros [async deftest is testing] :as t]))

(deftest normalisation-removes-spaces-test
  (let [input "  good test "
        expected "goodtest"
        result (sut/normalise-string input)]
    (is (= expected result))))

(deftest strings-return-1-if-they-are-both-empty-test
  (let [str1 ""
        str2 ""
        expected 1.0
        result (sut/trivial-comparison-results str1 str2)]
    (is (= expected result))))

(deftest strings-return-0-if-one-is-empty-test
  (let [str1 "full"
        str2 ""
        expected 0.0
        result (sut/trivial-comparison-results str1 str2)]
    (is (= expected result))))

(deftest identical-strings-return-1-test
  (let [str1 "twin"
        str2 "twin"
        expected 1.0
        result (sut/trivial-comparison-results str1 str2)]
    (is (= expected result))))

(deftest different-1-letter-strings-return-0-test
  )(let [str1 "a"
         str2 "z"
         expected 0.0
         result (sut/trivial-comparison-results str1 str2)]
     (is (= expected result)))

(deftest either-is-1-letter-string-returns-0-test
  (let [str1 "longer"
        str2 "d"
        expected 0.0
        result (sut/trivial-comparison-results str1 str2)]
    (is (= expected result))))

(deftest bigram-generation-test
  (let [input "bigram"
        expected ["bi" "ig" "gr" "ra" "am"]
        result (sut/bigrams input)]
    (is (= expected result))))

(deftest bigram-frequency-test
  (let [input "hahabo"
        expected {"ha" 2
                  "ah" 1
                  "ab" 1
                  "bo" 1}
        result (sut/bigram-frequencies input)]
    (is (= expected result))))

(deftest multi-string-comparison-test
  (let [input [{:str1 "french" :str2 "quebec" :expected 0}
               {:str1 "france" :str2 "france" :expected 1}
               {:str1 "fRaNce" :str2 "france" :expected 0.2}
               {:str1 "healed" :str2 "sealed" :expected 0.8}
               {:str1 "web applications" :str2 "applications of the web" :expected 0.7878787878787878}
               {:str1 "this will have a typo somewhere" :str2 "this will huve a typo somewhere" :expected 0.92}
               {:str1 "Olive-green table for sale, in extremely good condition." :str2 "For sale: table in very good  condition, olive green in colour." :expected 0.6060606060606061}
               {:str1 "Olive-green table for sale, in extremely good condition." :str2 "For sale: green Subaru Impreza, 210,000 miles" :expected 0.2558139534883721}
               {:str1 "Olive-green table for sale, in extremely good condition." :str2 "Wanted: mountain bike with at least 21 gears." :expected 0.1411764705882353}
               {:str1 "this has one extra word" :str2 "this has one word" :expected 0.7741935483870968}
               {:str1 "a" :str2 "a" :expected 1}
               {:str1 "a" :str2 "b" :expected 0}
               {:str1 "" :str2 "" :expected 1}
               {:str1 "a" :str2 "" :expected 0}
               {:str1 "" :str2 "a" :expected 0}
               {:str1 "apple event" :str2 "apple     event" :expected 1}
               {:str1 "iphone" :str2 "iphone x" :expected 0.9090909090909091}]
        results (map #(sut/compare-strings (:str1 %) (:str2 %)) input)]
    (is (= (map :expected input) results))))

(deftest get-ratings-multi-result-test
  (let [target "healed"
        input-options ["mailed" "edward" "sealed" "theatre"]
        expected [{:target "mailed" :rating 0.4}
                  {:target "edward" :rating 0.2}
                  {:target "sealed" :rating 0.8}
                  {:target "theatre" :rating 0.36363636363636365}]
        result (sut/find-best-match target input-options)]
    (is (= expected (:ratings result)))))

(deftest find-best-match-test
  (let [target "healed"
        input-options ["mailed" "edward" "sealed" "theatre"]
        expected {:target "sealed" :rating 0.8}
        result (sut/find-best-match target input-options)]
    (is (= expected (:best-match result)))))
