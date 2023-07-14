import sqlite3

# Connect to the database
conn = sqlite3.connect('ORADBLN2')
cursor = conn.cursor()
conn1 = sqlite3.connect('ORADBLN3')
cursor1 = conn1.cursor()

# Read entities from the source table
cursor.execute('SELECT * FROM crdsParty')
entities = cursor.fetchall()

# Create the destination table
cursor1.execute('CREATE TABLE IF NOT EXISTS Party (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)')

# Insert entities into the destination table
for entity in entities:
    cursor1.execute('INSERT INTO Party (id, name, age) VALUES (?, ?, ?)', entity)

# Commit the changes and close the connection
conn.commit()
conn.close()
