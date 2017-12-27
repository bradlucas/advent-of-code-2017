(ns advent.day20.swarm
  (:require [clojure.string :as str]))


(def s "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>\np=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>")

(def input-file "src/advent/day20/day20.txt")

;; {:p {:x 0 :y 0 :z 0}
;;  :v {:x 0 :y 0 :z 0}
;;  :a {:x 0 :y 0 :z 0}
;;  :dist 0}


;; {:p {:x 3 :y 0 :z 0} :v {:x 2 :y 0 :z 0} :a {:x -1 :y 0 :z 0} :dist 0}
;; {:p {:x 4 :y 0 :z 0} :v {:x 2 :y 0 :z 0} :a {:x -1 :y 0 :z 0} :dist 0}

(def t [{:p {:x 3 :y 0 :z 0} :v {:x 2 :y 0 :z 0} :a {:x -1 :y 0 :z 0} :dist 0}
        {:p {:x 3 :y 0 :z 0} :v {:x 2 :y 0 :z 0} :a {:x -1 :y 0 :z 0} :dist 0}
        {:p {:x 4 :y 0 :z 0} :v {:x 2 :y 0 :z 0} :a {:x -1 :y 0 :z 0} :dist 0}])

(defn build-xyz [s]
  (zipmap [:x :y :z] (map #(Integer/parseInt %) (str/split (str/trim (second (re-find #".=<(.*)>" s))) #","))))

(defn abs [n] (if (neg? n) (- n) n))

;; Manhattan distance
;; (+ (abs x) (abs y) (abs z))
(defn calc-distance [{x :x y :y z :z}]
  (+ (abs x) (abs y) (abs z)))

(defn parse-line [s]
  (let [[p v a] (str/split s #", ")
        rtn {:p (build-xyz p)
             :v (build-xyz v)
             :a (build-xyz a)}]
    (assoc rtn :dist (calc-distance (:p rtn)))))
     
(defn load-data [s]
  (mapv parse-line (str/split s #"\n")))


(defn map-add [m1 m2]
  (reduce
   (fn [{x1 :x y1 :y z1 :z}  {x2 :x y2 :y z2 :z}]
     {:x (+ x1 x2) :y (+ y1 y2) :z (+ z1 z2)})
   [m1 m2]))

(defn update-data-row [{p :p v :v a :a dist :dist }]
  ;; Increase velocity by the acceleration
  ;; Then update the position by the velocity
  ;; Lastly update the distance
  (let [v (map-add v a)
        p (map-add p v)
        dist (calc-distance p)]
    {:p p
     :v v
     :a a
     :dist dist}))

(defn update-data [data]
  ;; for each row in data
  (map update-data-row data))
  
(defn min-dist [data]
  ;; return index of particle
  ;; find the position of the particle with the minimum distance
  (let [indexed-data (map (fn [c v] [c v]) (range) data)]
    (reduce (fn [[idx-a map-a] [idx-b map-b]] (if (< (:dist map-a) (:dist map-b)) [idx-a map-a] [idx-b map-b])) indexed-data)
  ))

(defn process-till-max-cnt [data max-cnt]
  (loop [d data
         mins []
         cnt 0]
    (if (> cnt max-cnt)
      mins
      (recur (update-data d) (conj mins (first (min-dist d))) (inc cnt)))
    ))

(defn process [data]
  (loop [d data
         mins []
         cnt 0]
    (if (and (> (count mins) 300) (apply = (take-last 300 mins)))
      (last mins)
      (recur (update-data d) (conj mins (first (min-dist d))) (inc cnt)))
    ))

(defn run-part1 []
  (let [data (load-data (slurp input-file))]
    (process data)))
;; 170


;; Part 2
;; On each iteration remove particles that have the same position
;; Iterate/loop until there are no collisions

(defn no-collisions [data]
  ;; return true if all the points are unique
  ;; Add all data points to a set and compare the number of the set and the number of data points
  (let [data-count (count data)
        set-count (count (into #{} (map :p data)))]
    (= data-count set-count)))

(defn collisions [data]
  (not (no-collisions data)))

(defn not-in-points [points point]
  ;; return true if point is not in points
  (not (points point)))

(defn remove-collisions [data]
  ;; which points have duplicates
  (if-let [points (into #{} (map key (filter (fn [m] (> (count (val m)) 1)) (group-by :p data))))]
    (filter #(not-in-points points (:p %)) data)))

(defn process2 [data]
  (loop [d data
         mins []
         cnt 0]
    (if (and (> (count mins) 300) (apply = (take-last 300 mins)))
      (count d)
      (let [d2 (if (collisions d) (remove-collisions d) d)]
        (recur (update-data d2) (conj mins (first (min-dist d))) (inc cnt))))))

(defn run-part2 []
  (let [data (load-data (slurp input-file))]
    (process2 data)))
;; 571
