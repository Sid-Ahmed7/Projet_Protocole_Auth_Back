#!/bin/bash

login_response=$(curl -s -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"testuser@example.com","password":"Password@123"}')

echo "Réponse de l'API : $login_response"

token=$(echo $login_response | jq -r '.token')

if [ "$token" == "null" ] || [ -z "$token" ]; then
  echo "Erreur : Le token n'a pas été récupéré. Vérifiez les identifiants ou la réponse du serveur."
  exit 1
fi

echo "Token JWT récupéré : $token"

sed -i "s|{token}|$token|g" ./targets.txt
