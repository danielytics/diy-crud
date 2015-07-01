(ns crud-talk.core
  (:require
    [bidi.ring :as bidi-ring]
    [yesql.core :refer [defqueries]]
    [erinite.template.core :as t]
    [hiccup.core :refer [html]]))

;; Setup the database connection
(def db-spec {:classname "org.sqlite.JDBC"
              :subprotocol "sqlite"
              :subname "db.sqlite"})

;; Load database queries using yesql
(defqueries "queries.sql")

;; Create our templates
(def item-list-template
  [:div
    [:h1 "Item List"]
    [:table.items
      [:tr
        [:td.id]
        [:td.name]
        [:td.quantity]
        [:td
          [:form
            {:method "post"}
            [:button {:type "submit"} "delete"]]]]]
    [:hr]
    [:div "New item"]
    [:form#new
      {:action "/items"
       :method "post"}
      [:input.name {:type "text"}]
      [:input.description {:type "text"}]
      [:input.quantity {:type "text"}]
      [:button {:type "submit"} "add"]]])


(def item-list-transformations
  {[:.items]            [:clone-for :items]
   [:.items :.id]       [:content :id]
   [:.items :.name]     [:content :name]
   [:.items :.quantity] [:content :quantity]
   [:.items :form]      [:set-attr :action :url]})


;; Compile the template/transformation pair into a render function
(def item-list (t/compile-template
                 item-list-template
                 item-list-transformations))

;; Define handler functions

(defn read-items [request]
  (let [items (map
                #(assoc % :url (str "/item/" (:id %)))
                (get-all-items db-spec))]
    {:status 200
     :body  (html (item-list {:items items}))}))

(defn create-item [request]
  (println "create:" request))

(defn read-item [request]
  (println "read:" request))

(defn update-item [request]
  (println "put:" request))

(defn delete-item [request]
  (let [item-id (get-in request [:route-params :item-id])]
    ;; Delete the item from database
    (delete-item! db-spec item-id)
    ;; Redirect back to item list
    {:status  302
     :headers {"Location" "/items"}}))


;; Define URL routes

(def routes
  ["/" {"items"
          {{:request-method :get}   read-items
           {:request-method :post}  create-item}
        ["item/" :item-id]
          {{:request-method :get}   read-item
          {:request-method :put}    update-item
          {:request-method :post}   delete-item ;; Only here so we can test in browser with forms.. 
          {:request-method :delete} delete-item}}])

;; Hook it all up in a Ring handler

(def handler (bidi-ring/make-handler routes))

