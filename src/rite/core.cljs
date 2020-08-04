(ns rite.core
  (:require [clojure.string :as str]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(defn normalise-string
  [s]
  (str/replace s #"\s+" ""))

(defn trivial-comparison-results
  [str1 str2]
  (cond
    (and (empty? str1) (empty? str2)) 1.0
    (or (empty? str1) (empty? str2)) 0.0
    (= str1 str2) 1.0
    (and (= 1 (count str1)) (= 1 (count str2))) 0.0
    (or (= 1 (count str1)) (= 1 (count str2))) 0.0
    :else nil)
  )

(defn bigrams
  [s]
  (->> s
       (partition 2 1)
       (map #(apply str %))))

(defn bigram-frequencies
  [s]
  (-> s
      bigrams
      frequencies))

(defn compare-strings
  [str1 str2]
  (let [[s1 s2] (map normalise-string [str1 str2])]
    (if-let [trivial-result (trivial-comparison-results s1 s2)]
      trivial-result
      (let [[bf1 bf2] (map bigram-frequencies [s1 s2])
            x (->> bf2
                   (reduce-kv (fn [m k v]
                                (if-let [bf1-count (get bf1 k)]
                                  (let [this-intersection (min bf1-count v)]
                                    (assoc m :intersection (+ (:intersection m) this-intersection)))
                                  m)) {:intersection 0})
                   :intersection)]
        (/ (* 2.0 x) (- (+ (count s1) (count s2)) 2))))))

(defn find-best-match
  [str strs]
  (let [ratings (map (fn [x] (let [rating (compare-strings str x)]
                               {:target x
                                :rating rating})) strs)
        best-match (->> ratings
                        (sort-by :rating)
                        last)]
    {:ratings ratings
     :best-match best-match}))

(defn ^:export findBestMatch
  [str strs]
  (->>
   (find-best-match str strs)
   (cske/transform-keys csk/->camelCaseString)
   clj->js))
