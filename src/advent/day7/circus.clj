(ns advent.day7.circus
  (:require [clojure.set]
            [clojure.pprint]))


;; pbga (66)
;; xhth (57)
;; ebii (61)
;; havc (66)
;; ktlj (57)
;; fwft (72) -> ktlj, cntj, xhth
;; qoyq (66)
;; padx (45) -> pbga, havc, qoyq
;; tknk (41) -> ugml, padx, fwft
;; jptl (61)
;; ugml (68) -> gyxo, ebii, jptl
;; gyxo (61)
;; cntj (57)


;;                 gyxo
;;               /     
;;          ugml - ebii
;;        /      \     
;;       |         jptl
;;       |        
;;       |         pbga
;;      /        /
;; tknk --- padx - havc
;;      \        \
;;       |         qoyq
;;       |             
;;       |         ktlj
;;        \      /     
;;          fwft - cntj
;;               \     
;;                 xhth


;; Node
;; {
;;   :name
;;   :weight
;;   :children
;; }


;; Root
;; Node who is not a child of any other node

(def url "./src/advent/day7/day7.txt")

(defn split-name [s]
  (let [[name right] (clojure.string/split s #" ")
        weight (subs right 1 (dec (count right)))]
    [name (Integer/parseInt weight)]
    )
  )

(defn split-children [s]
  (if s
    (map clojure.string/trim (clojure.string/split s #","))))

(defn parse-line [line]
  ;; omytneg (1366) -> hmvin, eppzprf, zrioi, sdlcqtb
  ;; dgjqciq (24)
  (let [[left right] (map clojure.string/trim (clojure.string/split line #"->"))
        [name weight] (split-name left)
        children (split-children right)]
    {
     :name name
     :weight weight
     :children children
     }
    )
  )


(defn load-data [url]
  (with-open [rdr (clojure.java.io/reader url)]
    (vec (map parse-line (line-seq rdr)))
    )
  )


(defn root-name [v]
  ;; find the node who is not in any of the nodes in the sequence
  ;; list of all names
  ;; list of all nodes with children
  ;; list of all children
  ;; which name is not in the list of children
  (let [names (into #{} (map :name v))
        children (into #{} (filter identity (flatten (map :children v))))]
    ;; which name in names is not in children
    (first (vec (clojure.set/difference names children)))))


(defn run-part1 []
  (root-name (load-data url)))
;; eugwuhl



;; Part 2
;; The weights of the children need to match their root
;; Find the node with the incorrect weight and figure out what it's weight should be



;; Find the node whose children's weight don't match it's own
(def urltest "./src/advent/day7/day7test.txt")


;; {:name root
;;  :weight 40
;;  :tree-weight
;;  :children [{:name
;;              :weight
;;              :tree-weight
;;              :children [{:name
;;                          :weight
;;                          :tree-weight
;;                          :children }
;;                         {:name
;;                          :weight
;;                          :tree-weight
;;                          :children }
;;                         ]
;;              }
;;             {:name
;;              :weight
;;              :tree-weight
;;              :children [{:name
;;                          :weight
;;                          :tree-weight
;;                          :children }
;;                         {:name
;;                          :weight
;;                          :tree-weight
;;                          :children }
;;                         ]
;;              }
;;             {:name
;;              :weight
;;              :tree-weight
;;              :children [
;;                         ]
;;              }
;;             ]
;;  }



;; call with (load-data url)
;; return list of 'nodes'
;; for example: {:name "padx", :weight 45, :children ("pbga" "havc" "qoyq")}
;; (defn build-tree [v]
;;   ;; loop over list finding roots and building the tree
  
;;   )

(defn get-node-by-name [v name]
  (first (filter #(= (:name %) name) v)))

(defn get-root-node [v]
  ;; (get-node v (root-name v))
  (get-node-by-name v (root-name v)))


;; (defn get-node-children [v node]
;;   (map #(get-node v %) (:children node)))

;; (defn children-weights [v n]
;;   (let [children (:children n)]
;;     (apply + (map :weight (map #(get-node v %) children)))))

(defn children-children-weights [v n]
  (let [children (:children n)]
    (if (nil? children)
      [(:weight n)]
      ;; return a list of the children and their tree weights
      (conj [] (:weight n) (map #(children-children-weights v (get-node-by-name v %)) children)))))
        
(defn get-tree-weight [v n]
  (apply + (flatten (children-children-weights v n))))


(defn get-child-nodes [v n]
  (map #(get-node-by-name v %) (:children n)))

;; (defn get-child-node-weights [v n]
;;   (map #(get-tree-weight v %) (get-child-nodes v n)))

;; (defn get-child-node-weights [v n]
;;   (map #(fn [node] (assoc node :tree-weight (get-tree-weight v node))) (get-child-nodes v n)))


(defn add-tree-weight-to-map [v node]
  (assoc node :tree-weight (get-tree-weight v node)))

(defn get-child-node-weights [v n]
  (map #(add-tree-weight-to-map v %) (get-child-nodes v n)))



                        
;; (defn find-nodes-childrens-weights [v n]
;;   ;; v is the vector of nodes
;;   ;; find n
;;   (loop [v v
;;          tree {}
;;          ]
;;     (if (nil? v)
;;       v)
;;     (recur nil nil)
;;     )
;;   )

(defn find-unbalanced [nodes]
  ;; return the node which has a 'different' :tree-weight
  (let [freqs (frequencies (map :tree-weight nodes))]
    (if (= 2 (count freqs))
      (let [sorted-freqs (sort-by val freqs)
            tw (first (first sorted-freqs))
            diff (- (first (second sorted-freqs)) (first (first sorted-freqs)))]
        ;; (println sorted-freqs tw diff)
        [(first (filter #(= (:tree-weight %) tw) nodes))
         diff]
         ))))

(defn run [v]
  (loop [r (get-root-node v)
         diff 0]
    (let [child-nodes (get-child-node-weights v r)]
      ;; find unblanced node
      ;; node with the :tree-weight that isn't the same as the others
      (let [[unbalanced child-diff] (find-unbalanced child-nodes)]
        ;; (println unbalanced child-diff)
        (if unbalanced
          (recur unbalanced child-diff)
          [r (+ (:weight r) diff)])
          ))))


(defn run-part2 []
  (let [[node new-weight] (run (load-data url))]
    new-weight))


(defn pp [o] (clojure.pprint/pprint o))

;; ----------------------------------------------------------------------------------------------------
;; 420 is the answer



;; advent.day7.circus> (pp (get-child-node-weights v r))
;; ({:name "avpklqy",
;;   :weight 68,
;;   :children ("werlnym" "tkoxc" "iquxck"),
;;   :tree-weight 48284}
;;  {:name "tytbgx",
;;   :weight 6588,
;;   :children ("ynseh" "uqqclia" "darwcoj" "eooiuq"),
;;   :tree-weight 48284}
;;  {:name "bdohoaa",
;;   :weight 18440,
;;   :children ("lxjbzu" "ppsnn" "hatfl"),
;;   :tree-weight 48284}
;;  {:name "smaygo",
;;   :Weight 4616,
;;   :children ("hmgrlpj" "fbnbt" "nfdvsc"),
;;   :tree-weight 48292}                                       <============== smaygo
;;  {:name "pvvbn",
;;   :weight 8732,
;;   :children ("adyni" "dvltfhm" "luerna"),
;;   :tree-weight 48284}
;;  {:name "hgizeb",
;;   :weight 5324,
;;   :children ("tfzsqj" "njibgv" "izjppbx"),
;;   :tree-weight 48284}
;;  {:name "tchfafn",
;;   :weight 31649,
;;   :children ("qrxso" "xoedja" "fapkonf"),
;;   :tree-weight 48284})

;; advent.day7.circus> (pp (get-child-node-weights v (get-node-by-name v "smaygo")))
;; ({:name "hmgrlpj",
;;   :Weight 66,
;;   :children
;;   ("pnqrgfk" "luasrvp" "ahrfh" "tbfce" "drjmjug" "nigdlq" "omytneg"),
;;   :tree-weight 14564}                                     <================ hmgrlpj
;;  {:name "fbnbt",
;;   :weight 5262,
;;   :children ("lcmek" "aflzz" "rqgccu" "vxfpzku" "yvixrl" "ojkhavf"),
;;   :tree-weight 14556}
;;  {:name "nfdvsc",
;;   :weight 4824,
;;   :children ("kenpyrt" "ytqrut" "pustj" "hbymky" "tpvvbr" "lcfsu"),
;;   :tree-weight 14556})
;; nil
;; advent.day7.circus> (pp (get-child-node-weights v (get-node-by-name v "hmgrlpj")))
;; ({:name "pnqrgfk",
;;   :weight 1707,
;;   :children ("msekejl" "aaroa" "tfregkr"),
;;   :tree-weight 2070}
;;  {:name "luasrvp",
;;   :weight 1476,
;;   :children ("mnvlfcf" "ofxag" "xzcfko"),
;;   :tree-weight 2070}
;;  {:name "ahrfh",
;;   :weight 2040,
;;   :children ("yyfrmb" "actlu"),
;;   :tree-weight 2070}
;;  {:name "tbfce",
;;   :weight 5,
;;   :children ("vozygf" "mjmskc" "rludm" "wgjrzd" "pwdziu"),
;;   :tree-weight 2070}
;;  {:name "drjmjug",
;;   :Weight 428,
;;   :children ("goiwg" "sbogacc" "jdvxawc" "wlczi" "vinbva"),
;;   :tree-weight 2078}                                   <================== drjmjug
;;  {:name "nigdlq",
;;   :weight 846,
;;   :children ("hdsth" "hlxlrjq" "txyqy" "myerj" "fmntbo" "irkfc"),
;;   :tree-weight 2070}
;;  {:name "omytneg",
;;   :weight 1366,
;;   :children ("hmvin" "eppzprf" "zrioi" "sdlcqtb"),
;;   :tree-weight 2070})
;; nil
;; advent.day7.circus> (pp (get-child-node-weights v (get-node-by-name v "drjmjug")))        <======== Has balanced children so you need to adjust the parent drjmjug
;; ({:name "goiwg",
;;   :weight 162,
;;   :children ("lxngrgu" "ovtoke" "mpmmos"),
;;   :tree-weight 330}
;;  {:name "sbogacc",
;;   :weight 264,
;;   :children ("qcsjl" "wttimqr"),
;;   :tree-weight 330}
;;  {:name "jdvxawc",
;;   :weight 26,
;;   :children ("kfyaw" "kglxbse" "ukkmfh" "jyoevqm"),
;;   :tree-weight 330}
;;  {:name "wlczi",
;;   :weight 246,
;;   :children ("yxotxz" "xronh"),
;;   :tree-weight 330}
;;  {:name "vinbva",
;;   :weight 278,
;;   :children ("jpjezb" "zfswj"),
;;   :tree-weight 330})


;; Solution
;; drjmjug has balanced child to so balance it with it's peers
;; it needs to be reduced by 8
;; It's children weight 2078 while it's peers have trees of 2070
;;
;; Reduce 428 to 420
