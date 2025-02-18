#!/bin/bash

echo "Effectuer l'inscription de l'utilisateur..."
register_response=$(curl -s -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{"id": 6, "email":"test@example.com","password":"5Sa!Api85","username":"testUser"}')

echo "Réponse de l'API pour l'inscription : $register_response"

if [[ "$register_response" == *"error"* ]]; then
  echo "Erreur : L'inscription a échoué. Réponse de l'API : $register_response"
  exit 1
fi

echo "Effectuer la requête de login pour récupérer le token JWT..."
login_response=$(curl -s -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"5Sa!Api85"}')

echo "Réponse de l'API pour la connexion : $login_response"

token=$(echo $login_response | jq -r '.token')

if [ "$token" == "null" ] || [ -z "$token" ]; then
  echo "Erreur : Le token n'a pas été récupéré. Vérifiez les identifiants ou la réponse du serveur."
  exit 1
fi

token=$(echo "$token" | tr -d '[:space:]')

echo "Token JWT récupéré : $token"

if [ ! -f ./targets.txt ]; then
  echo "Erreur : Le fichier targets.txt est introuvable."
  exit 1
fi

sed -i "s|{token}|$token|g" ./targets.txt

echo "Token JWT a été inséré dans targets.txt."


vegeta attack -duration=30s -rate=5 --targets=./targets.txt | tee results.bin | vegeta report

if [ ! -f results.bin ]; then
  echo "❌ Erreur : results.bin n'a pas été créé ! Vérifie ton test Vegeta."
  exit 1
fi

echo "Génération du graphique interactif..."
vegeta plot results.bin > plot.html

echo "✅ Graphique généré : Ouvre plot.html dans ton navigateur !"
