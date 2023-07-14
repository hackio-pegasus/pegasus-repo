import sqlite3

# Connect to the database
conn = sqlite3.connect('ORADBLN3')
cursor = conn.cursor()
conn1 = sqlite3.connect('ORADBLN4')
cursor1 = conn1.cursor()

# Read entities from the source table
cursor.execute('SELECT * FROM party')
entities = cursor.fetchall()

# Create the destination table
cursor1.execute('CREATE TABLE IF NOT EXISTS dim_party (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)')

# Insert entities into the destination table
for entity in entities:
    cursor1.execute('INSERT INTO dim_party (id, name, age) VALUES (?, ?, ?)', entity)

# Commit the changes and close the connection
conn.commit()
conn.close()
