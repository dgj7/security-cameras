#!/usr/bin/env python3

import hashlib
import os
import base64
from datetime import datetime
import getpass

###############################################################################
# load the configuration
###############################################################################
def loadConfig(filepath, sep='=', comment_char='#'):
    props = {}
    try:
        with open(filepath, "rt") as f:
            for line in f:
                l = line.strip()
                if l and not l.startswith(comment_char):
                    key_value = l.split(sep)
                    key = key_value[0].strip()
                    value = sep.join(key_value[1:]).strip().strip('"')
                    props[key] = value
    except FileNotFoundError:
        print("config file not found, please provide credentials.")
    return props

###############################################################################
# find the username
###############################################################################
def findUsername(dict):
    result = dict.get('username', None)
    if (result == None):
        return input("username:")
    else:
        return result

###############################################################################
# find the password
###############################################################################
def findPassword(dict):
    result = dict.get('password', None)
    if (result == None):
        return getpass.getpass("password:")
    else:
        return result
    
###############################################################################
# find the ip addr
###############################################################################
def findIp(dict):
    result = dict.get('ipaddr', None)
    if (result == None):
        return input("ip:")
    else:
        return result

###############################################################################
# main program entrypoint
###############################################################################
# load the configuration
config = loadConfig("config.cfg")

# get credentials
username = findUsername(config)
password = findPassword(config)
ipaddr = findIp(config)

# grab timestamp
created = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.000Z")

# set username/password fields
raw_nonce = os.urandom(20)
nonce = base64.b64encode(raw_nonce)
sha1 = hashlib.sha1()
sha1.update(raw_nonce + created.encode('utf8') + password.encode('utf8'))
raw_digest = sha1.digest()
digest = base64.b64encode(raw_digest)

# generate template soap envelope
template = """<s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope">
    <s:Header>
        <Security s:mustUnderstand="1" xmlns="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
            <UsernameToken>
                <Username>{username}</Username>
                <Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">{digest}</Password>
                <Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">{nonce}</Nonce>
                <Created xmlns="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">{created}</Created>
            </UsernameToken>
        </Security>
    </s:Header>
    <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <GetCapabilities xmlns="http://www.onvif.org/ver10/device/wsdl">
            <Category>All</Category>
        </GetCapabilities>
    </s:Body>
</s:Envelope>"""

# create the soap request body
req_body = template.format(username=username, nonce=nonce.decode('utf8'), created=created, digest=digest.decode('utf8'))

# print the soap request body
print(req_body)
