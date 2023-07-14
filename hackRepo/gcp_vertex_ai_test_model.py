import os
import requests
import json
import openai

openai.api_key = AZURE_OPENAI_KEY
openai.api_base = AZURE_OPENAI_ENDPOINT
openai.api_type = 'azure'
openai.api_version = '2023-05-15'

def call_openai(txt):
    response = openai.ChatCompletion.create(
        engine="gpt-35-turbo",
        messages = [{"role":"system","content":"You are an AI assistant that helps people find information."},{"role":"user","content": txt}],
        temperature=0.7,
        max_tokens=3130,
        top_p=0.95,
        frequency_penalty=0,
        presence_penalty=0,
        stop=None
    )
    text = response['choices'][0]['message']['content'].strip()
    return text

import os

#!git clone https://github.com/derickfelix/BankApplication.git
!git clone https://github.com/hackio-pegasus/pegasus-repo.git
def get_all_files(directory):
    file_list = []
    dict = {}
    for root, dirs, files in os.walk(directory):
        dirs[:] = [d for d in dirs if not "." in d]
        files = [ file for file in files if not file.endswith(('CRMPortalInstanceLifecycleListener.java')) ]
        for file in files:
            with open(os.path.join(root, file), 'r') as f:
                cont = f.read()
                print(cont)
                response = call_openai("I am sending code from file - " + file + ". Please read it and list down all inbound and outbound external connection details and represent it in json format. key should be the filename. - \n " + cont)
                print(response)
            #file_list.append(os.path.join(root, file))
    # return dict

# Example usage:
directory_path = 'pegasus-repo'
dict = get_all_files(directory_path)

#print("Updated Dict is: ", dict)
# json_object = json.dumps(dict, indent = 4) 
# print(json_object)

#response = call_openai("in the below json, key is the filename and value is the content of the file. Please read these files and tell me how the data is flowing." + json_object)
#print(response)

# Print the list of files
#for file in files:
#    print(file)
