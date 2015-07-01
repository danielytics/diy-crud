-- name: get-all-items
-- Read a summary list of all items in database.
SELECT id, name, quantity
FROM items

-- name: get-item
-- Read a specific item from database.
SELECT name, quantity, description
FROM items
WHERE id = :id

-- name: add-item!
-- Create a new item.
INSERT INTO items
(id, name, description, quantity)
VALUES (:id, :name, :description, :quantity)

-- name: delete-item!
-- Delete a specific item from the database.
DELETE FROM items
WHERE id = :id
