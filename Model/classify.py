from flask import Flask, request, jsonify
import torch
from transformers import BertTokenizer, BertForSequenceClassification

app = Flask(__name__)

# Load trained BERT model
model_path = "bert-email-model"
tokenizer = BertTokenizer.from_pretrained(model_path)
model = BertForSequenceClassification.from_pretrained(model_path)
model.eval()

# Email categories
categories = ["Sales", "Account", "Service", "CRM","Purchase","Others",]

def classify_email(content):
    inputs = tokenizer(content, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    
    logits = outputs.logits
    predicted_class = torch.argmax(logits, dim=1).item()
    
    # Debugging logs
    print("📩 Email Content:", content)
    print("🔢 Predicted Class Index:", predicted_class)
    print("✅ Available Categories:", categories)
    print("📊 Logits:", logits.tolist())
    
    # Ensure the predicted class is within range
    if predicted_class < 0 or predicted_class >= len(categories):
        print("❌ ERROR: Invalid category index")
        return "Unknown"

    return categories[predicted_class]

@app.route("/classify", methods=["POST"])
def classify():
    data = request.json
    content = data.get("content", "").strip()
    
    if not content:
        return jsonify({"error": "Empty content"}), 400
    
    category = classify_email(content)
    return jsonify({"category": category})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)