(ns advent.day24.moat
  (:require [clojure.string :as str]))

(def url "src/advent/day24/day24.txt")

(def test-str "0/2\n2/2\n2/3\n3/4\n3/5\n0/1\n10/1\n9/10")

(defn load-data [s]
  (->> (str/split-lines s)
       (map #(str/split % #"/"))
       (map (fn [[a b]] [(Integer/parseInt a) (Integer/parseInt b)]))))

;; 0/2
;; 2/2
;; 2/3
;; 3/4
;; 3/5
;; 0/1
;; 10/1
;; 9/10

;; ([0 2] [2 2] [2 3] [3 4] [3 5] [0 1] [10 1] [9 10])
;; For each A build all potential sets of bridges


;; 0/2
;; 0/1
;; 2/2
;; 2/3
;; 3/4
;; 3/5
;; 9/10
;; 10/1

;; 0/1
;; 0/1--10/1
;; 0/1--10/1--9/10
;; 0/2
;; 0/2--2/3
;; 0/2--2/3--3/4
;; 0/2--2/3--3/5
;; 0/2--2/2
;; 0/2--2/2--2/3
;; 0/2--2/2--2/3--3/4
;; 0/2--2/2--2/3--3/5

;; Order of connectors doesn't matter but it appears that one of a/b must match to connect
;; Also, all bridges need to start with 0 to connect with your side of the pit


;; Build a tree of possibilities
;; Remember each connector can connect via either side
;; For example a 10/1 can work as a 1/10

;; (build-index (load-date s))
;; @see bhauman https://github.com/bhauman/advent-of-clojure-2016/blob/master/src/advent_of_clojure_2017/day24.clj
(defn build-index [data]
  (->> data
       (reduce (fn [accum [h t :as part]]
                 (-> accum
                     (update h (fnil conj #{}) part)
                     (update t (fnil conj #{}) part)))
               {})))


;; Build bridges
;;

;; (clojure.pprint/pprint (sort (build-index (load-data test-str))))
;; ([0 #{[0 2] [0 1]}]
;;  [1 #{[10 1] [0 1]}]
;;  [2 #{[2 2] [2 3] [0 2]}]
;;  [3 #{[2 3] [3 4] [3 5]}]
;;  [4 #{[3 4]}]
;;  [5 #{[3 5]}]
;;  [9 #{[9 10]}]
;;  [10 #{[10 1] [9 10]}])

(defn remove-component [components [a b :as component]]
  (-> components
      (update a disj component)
      (update b disj component)))

(defn other-component [h? [h t]]
  (if (= h? h) t h))

(defn bridges [components component idx]
  (if (empty? (components idx))
    [[component]]
    (->> (components idx)
         (mapcat #(bridges (remove-component components %) % (other-component idx %)))
         (mapv #(cons component %)))))


(defn strength [l]
  (apply + (flatten l)))


;; each A/B is a connector
;; The numbers are pins for each port
;; Order of ports is not important
;; 10/1 can connect with 9/10



(def components (build-index (load-data test-str)))


(defn pp [o]
  (clojure.pprint/pprint o))

(defn run-part1 []
  (->> (bridges (build-index (load-data (slurp url))) [0 0] 0)
      (map strength)
      (reduce max)))
;; 1940


;; Part 2
;; Whatis the strength of the longest bridge

(defn run-part2 []
  (let [bridges (bridges (build-index (load-data (slurp url))) [0 0] 0)
        max-length (reduce max (map count bridges))]
    (->> bridges
         (filter #(= (count %) max-length))   ;; pick out the longest one(S)
         (map strength)
         (reduce max))))
;; 1928

  
