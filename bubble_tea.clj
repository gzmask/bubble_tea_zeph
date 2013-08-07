; Gzmask's bubble tea zeph Windows Manager
; ~/.lein/profiles.clj: {:user {:plugins [[lein-exec "0.3.0"]]}}
; lein exec ~/.zeph/bubble_tea.clj

(use '[leiningen.exec :only (deps)])
;(use '[clojure.set :only (union)])
(deps '[[org.clojure/data.json "0.2.2"]])

(load-file "/Applications/Zephyros.app/Contents/Resources/libs/zephyros.clj")

(defn split2 [frame] 
  [{:x (:x frame)
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (:h frame)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (:h frame)}])

(defn split3 [frame] 
  [{:x (:x frame)
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (:h frame)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}])

(defn disj_windows [stack fwin]
  (disj (into #{} (for [win stack]
    (if (not= (get-window-title win) (get-window-title fwin))
      win))) nil))

(defn bubble [fwin stack frame]
  (let [stack_ (if fwin (disj_windows stack fwin) stack)
        stackv (if fwin (into [fwin] stack_) (into [] stack_))]
    (cond
      (= (count stackv) 1) (set-frame (first stackv) frame)
      (= (count stackv) 2) (do 
                             (set-frame (first stackv) (first (split2 frame))) 
                             (set-frame (second stackv) (second (split2 frame))))
      (= (count stackv) 3) (do 
                             (set-frame (first stackv) (first (split3 frame))) 
                             (set-frame (second stackv) (second (split3 frame)))
                             (set-frame (last stackv) (last (split3 frame))))
      :else (do 
              (set-frame (first stackv) (first (split3 frame))) 
              (set-frame (second stackv) (second (split3 frame)))
              (bubble 
                nil
                (into #{} (rest (rest stackv)))
                (last (split3 frame))
              )))))

(bind "h" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (bubble window stack frame))))

(bind "h" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-left window))))

(bind "l" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-right window))))

(bind "k" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-up window))))

(bind "j" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-down window))))


(bind "i" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)]
              (alert (str 
                       "focused:"
                       window
                       " get visible window:"
                       (seq (disj_windows (get-visible-windows) window))
                       ) 3)
              )))


@listen-for-callbacks ;; necessary when you use (bind) or (listen)
