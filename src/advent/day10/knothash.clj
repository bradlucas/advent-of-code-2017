(ns advent.day10.knothash)


(def url "./src/advent/day10/day10.txt")

(defn load-data [url]
  (mapv #(Integer/parseInt %) (clojure.string/split (clojure.string/trim (slurp url)) #",")))

;; list of numbers 0 to 255
;; current position 0
;; skip size 0
;; sequence of lengths (the input)

;; For each length:
;; - reverse the order of that length of elements in the list starting with the element at the current position
;; - move the current position forward by that length plus the skip size
;; - increase the skip size by one

(defn build-ary []
  (vec (range 256)))

;; @see https://github.com/thegeez/clj-advent-of-code-2017/blob/master/src/advent/day10.clj
(defn swap-indexes [start-idx length rope-count]
  (let [half-length (long (Math/ceil (/ length 2)))
        from-indexes (range start-idx
                            (inc (+ start-idx half-length)))
        to-indexes (range (dec (+ start-idx length))
                          (dec (- (+ start-idx length)
                                  half-length)) -1)
        wrap (fn [n] (mod n rope-count))]
    (zipmap (map wrap from-indexes)
            (map wrap to-indexes))))

(defn swap [v [from to]]
  (let [f (get v from)
        t (get v to)]
    (assoc v
           from t
           to f)))

(defn circular-reverse-length [ary cur len]
  ;; reverse the 'len' number of items of ary starting at cur position
  (let [start cur
        end (mod (+ cur len) (count ary))]
    ;; (println start end)
    (reduce swap
            ary
            (swap-indexes cur len (count ary)))))

(defn circular-add [total-length cur length skip]
  ;; add cur len and skip and wrap over total-length
  (let [amt (+ length skip)]
    (mod (+ cur amt) total-length)))

(defn process-helper [ary len cur skip]
  (let [ary (circular-reverse-length ary cur len)
        cur (circular-add (count ary) cur len skip)
        skip (inc skip)]
    [ary cur skip]))

(defn process [ary input]
  (loop [ary ary
         input input
         cur 0
         skip 0]
    ;; (println ary input cur skip)
    (if (empty? input)
      ary
      (let [[a c s] (process-helper ary (first input) cur skip)]
        (recur a (rest input) c s)))))

(defn run-part1 []
  (let [v (load-data url)]
    (let [r (process (build-ary) v)]
      (* (first r) (second r)))))
;; 11375


;; ----------------------------------------------------------------------------------------------------
;; Part 2
;; Consider the input numbers as ASCII
;; 1,2,3, == 49 44 50 44 51 44

;; (defn to-ascii [s]
;;   ;; read each character as an ascii value
;;   (mapv int s))

;; (defn add-suffix [v]
;;   (flatten (merge v [17 31 73 47 23])))

;; Doesn't work
;; (defn process2 [input [ary cur skip]]
;;   (loop [input input
;;          ary ary
;;          cur 0
;;          skip 0]
;;     (if (empty? input)
;;       [ary cur skip]
;;       (let [[a c s] (process-helper ary (first input) cur skip)]
;;         (recur (rest input) a c s)))))

(defn process2 [ary cur l]
  (circular-reverse-length ary cur l))

;; --------------------------------------------------
;; Found some help at https://github.com/borkdude/aoc2017/blob/master/src/day10.clj
(defn solve
  [input init]
  (reduce
   (fn [[nums pos skip-size] l]
     [(process2 nums pos l)
      (+ pos l skip-size)
      (inc skip-size)])
   init
   input))

(defn run-ropes [s]
  (let [v (concat (map int s) [17 31 73 47 23])
        ary (build-ary)]
    (first (nth (iterate
                 (partial solve v)
                 [ary 0 0])
                64))))

(defn bitwise-xor [s]
  ;; (bitwise-xor [65  27  9  1  4  3  40  50  91  7  6  0  2  5  68  22])
  ;; 64
  (reduce (fn [l r] (bit-xor l r)) s))

(defn build-dense-hash [r]
  (let [parts (partition 16 r)]
    (map bitwise-xor parts)))

;; @see https://github.com/thegeez/clj-advent-of-code-2017/blob/master/src/advent/day10.clj
(def to-hex-char (zipmap (range 16) "0123456789abcdef"))

(defn dec-to-hex [n]
  ;;  64 => 40
  ;;   7 => 07
  ;; 255 => ff
  (let [q (quot n 16)
        r (rem n 16)]
    (apply str (map to-hex-char [q r]))))

;; add 17, 31, 73, 47, 23    
;;(flatten (merge (mapv int (clojure.string/trim "1,2,3,")) [17 31 73 47 23]))

;; 65 ^ 27 ^ 9 ^ 1 ^ 4 ^ 3 ^ 40 ^ 50 ^ 91 ^ 7 ^ 6 ^ 0 ^ 2 ^ 5 ^ 68 ^ 22  = 64
;; [65  27  9  1  4  3  40  50  91  7  6  0  2  5  68  22]

(defn part2 [s]
  (apply str (map dec-to-hex (build-dense-hash (run-ropes s)))))

(defn run-test []
  (and
   (= (part2 "") "a2582a3a0e66e6e86e3812dcb672a272")
   (= (part2 "AoC 2017") "33efeb34ea91902bb2f59c9920caa6cd")
   (= (part2 "1,2,3") "3efbe78a8d82f29979031a4aa0b16a9d")
   (= (part2 "1,2,4") "63960835bcdc130f0b66d7ff4f6a5a8e")))


(defn run-part2 []
  (part2 (clojure.string/trim (slurp url))))
;; e0387e2ad112b7c2ef344e44885fe4d8



