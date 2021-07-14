(ns chess.printing
  (:require [chess.common :as c]))

(defn colors
  [i]
  (if (even? i)
    {:fa c/ANSI_BLACK
     :ba c/ANSI_WHITE_BG
     :fb c/ANSI_WHITE
     :bb c/ANSI_BLACK_BG}
    {:fa c/ANSI_WHITE
     :ba c/ANSI_BLACK_BG
     :fb c/ANSI_BLACK
     :bb c/ANSI_WHITE_BG}))

(defn print-rank
  [i rank]
  (let [colors (colors i)
        square-colors (cycle [[:ba :fa] [:bb :fb]])
        nc c/ANSI_NC]
    (println
     (->> (mapv (fn [r [f b]]
                  (str (get colors b) (get colors f) " " r " "))
                rank
                square-colors)
          (#(conj % nc))
          (apply str)))))

(defn print-board
  [fen]
  (doall
   (map-indexed print-rank (:state (c/fen->board fen))))
  fen)
