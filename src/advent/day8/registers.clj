(ns advent.day8.registers)

;; b inc 5 if a > 1
;; a inc 1 if b < 5
;; c dec -10 if a >= 1
;; c inc -20 if c == 10

;; [register operation value] if [register operator value]

;; For Part 1
;; :r2 :op2 :v2 :cmd :r1 :op1 :v1
;; :op2 == "inc" or "dec"
;;
;; :op1 == "<" "==" ">" "<=" "!=" ">="


(def url "./src/advent/day8/day8.txt")

(defn parse-line [line]
  (zipmap [:r2 :op2 :v2 :cmd :r1 :op1 :v1] (clojure.string/split line #" ")))

(defn load-data [url]
  (with-open [rdr (clojure.java.io/reader url)]
    (vec (map parse-line (line-seq rdr)))))

(defn truth [m line]
  ;; is :r1 :op1 :v1?
  (let [r1 (:r1 line)
        op1 (:op1 line)
        v1 (Integer/parseInt (:v1 line))

        value (get m r1 0)
        ]
    ;; (println r1 op1 v1 value)
    (cond
      (= op1 "<") (< value v1)
      (= op1 "==") (= value v1)
      (= op1 ">") (> value v1)
      (= op1 "<=") (<= value v1)
      (= op1 "!=") (not= value v1)
      (= op1 ">=") (>= value v1))))
   
(defn apply-command [m line]
  ;; :r2 :op2 :v2
  ;; :op2 == "inc" or "dec"
  (let [r2 (:r2 line)
        op2 (:op2 line)
        v2 (Integer/parseInt (:v2 line))
                             
        value (get m r2 0)]
    (if (= "inc" op2)
      (assoc m r2 (+ value v2))
      (if (= "dec" op2)
        (assoc m r2 (- value v2))
        (println "ERROR: Unknown :op2")))))

(defn process-line [m line]
  (if (truth m line)
    (apply-command m line)
    m))

(defn process [v]
  (loop [v v
         m {}]
    (if (empty? v)
      m
      (recur (rest v) (process-line m (first v))))))


(defn run-part1 []
  (let [v (load-data url)]
    (reduce max (vals (process v)))))
;; 4066


;; Part2
;; Track highest value ever held in a register



(defn find-highest [v highest]
  (let [val-list (vals v)]
    ;; (println (sort val-list))
    (if (nil? val-list)
      0
      (let [tmp (reduce max val-list)]
        (if (> tmp highest)
          tmp
          highest)))))

(defn process2 [v]
  (loop [v v
         m {}
         highest 0
         ]
    (if (empty? v)
      highest
      (recur (rest v) (process-line m (first v)) (find-highest m highest)))))

                 
(defn run-part2 []
  (let [v (load-data url)]
    (process2 v)))
;; 4829
