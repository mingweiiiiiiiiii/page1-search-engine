# Compile trec_eval.
echo "Compiling trec_eval..."
(cd trec_eval-9.0.7/ && make)

# Install python requirements.
echo "Installing python requirements..."
pip install -r requirements.txt
