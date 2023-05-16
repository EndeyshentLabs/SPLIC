(require '[clojure.string])

(def spl-keywords {
  :PLUS "+",
  :MINUS "-",
  :MULT "*",
  :DIV "/",
  :DUMP "!",
  :CHAR "@",
  :CHAR-NL "@:",
  :BREAK "#",
  :HALT "!#",
  :DEBUG "|"
})

(defn spl-kwd [k] (get spl-keywords k))

(def src (slurp "src.spl"))
;; (def words (clojure.string/split src #"[\s;()]+"))
(def words (clojure.string/split (clojure.string/replace src #"}.*[\r\n]" "") #"[\s]+"))

(def wait #())

(def spl-stack [])

(defn kwd [n] (def spl-stack (conj spl-stack n)))
(defn number [n] (def spl-stack (conj spl-stack n)))
(defn number* [n] (def spl-stack (into [(Double/toString n)] spl-stack)))

(defn make-kwd [id] (kwd id))
(defn make-number [n] (number n))
(defn make-error [i]
  (printf  "SPLIC.ERROR Unknown expresssion: `%s`.\n" i)
  (println "  \033[3mGood luck trying to find where this error happened.\033[0m")
  (System/exit 1)
)

(doseq [i words]
  (condp = (clojure.string/lower-case (clojure.string/trim i))
    (spl-kwd :PLUS)  (make-kwd i)
    (spl-kwd :MINUS) (make-kwd i)
    (spl-kwd :MULT)  (make-kwd i)
    (spl-kwd :DIV)   (make-kwd i)
    (spl-kwd :DUMP)  (make-kwd i)
    (spl-kwd :CHAR)  (make-kwd i)
    (spl-kwd :CHAR-NL)  (make-kwd i)
    (spl-kwd :DEBUG)  (make-kwd i)
    (spl-kwd :BREAK) (make-kwd i)
    (spl-kwd :HALT) (make-kwd i)
    (re-matches #"\d*" i) (make-number i)
    (re-matches #"-\d*" i) (make-number i)
    (make-error i)
  )
)

(defn drop-nth [n coll]
  (concat
    (take n coll)
    (drop (inc n) coll)
  )
)

(doseq [i spl-stack]
  (condp = i
    (spl-kwd :PLUS) :>> (fn [_]
                          (number*
                            (+
                             (read-string (nth spl-stack (- (.indexOf spl-stack i) 1)))
                             (read-string (nth spl-stack (- (.indexOf spl-stack i) 2)))
                            )
                          )
                          (def spl-stack (drop-nth 1 spl-stack))
                          (def spl-stack (drop-nth 1 spl-stack))
                          (def spl-stack (drop-nth 1 spl-stack))
                        )
    (spl-kwd :MINUS) :>> (fn [_]
                           (number*
                             (+
                              (read-string (nth spl-stack (- (.indexOf spl-stack i) 2)))
                              (read-string (nth spl-stack (- (.indexOf spl-stack i) 1)))
                             )
                           )
                           (def spl-stack (drop-nth 1 spl-stack))
                           (def spl-stack (drop-nth 1 spl-stack))
                           (def spl-stack (drop-nth 1 spl-stack))
                         )
    (spl-kwd :MULT) :>> (fn [_]
                          (number*
                            (* (read-string (nth spl-stack (- (.indexOf spl-stack i) 1)))
                               (read-string (nth spl-stack (- (.indexOf spl-stack i) 2))))
                          )
                          (def spl-stack (drop-nth 1 spl-stack))
                          (def spl-stack (drop-nth 1 spl-stack))
                          (def spl-stack (drop-nth 1 spl-stack))
                        )
    (spl-kwd :DIV) :>> (fn [_]
                         (number*
                           (* (read-string (nth spl-stack (- (.indexOf spl-stack i) 2)))
                              (read-string (nth spl-stack (- (.indexOf spl-stack i) 1))))
                         )
                         (def spl-stack (drop-nth 1 spl-stack))
                         (def spl-stack (drop-nth 1 spl-stack))
                         (def spl-stack (drop-nth 1 spl-stack))
                       )
    (spl-kwd :DUMP) :>> (fn [_]
                          (println (nth spl-stack (- (.indexOf spl-stack i) 1)))
                          (def spl-stack (drop 2 spl-stack))
                        )
    (spl-kwd :CHAR) :>> (fn [_]
                          (print
                            (char (read-string (nth spl-stack (- (.indexOf spl-stack i) 1))))
                          )
                          (def spl-stack (drop 2 spl-stack))
                        )
    (spl-kwd :CHAR-NL) :>> (fn [_]
                          (println
                            (char (read-string (nth spl-stack (- (.indexOf spl-stack i) 1))))
                          )
                          (def spl-stack (drop 2 spl-stack))
                        )
    (spl-kwd :BREAK) :>> (fn [_]
                           (System/exit 0)
                         )
    (spl-kwd :HALT) :>> (fn [_]
                          (System/exit 1)
                        )
    (spl-kwd :DEBUG) :>> (fn [_]
                           (println spl-stack)
                           (def spl-stack (drop 1 spl-stack))
                         )
    (wait) ;; Fall through
  )
)
