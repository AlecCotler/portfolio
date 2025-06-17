import sys
import random




#this program is expected to be run like
#python languageModel.py -i <readingFrom> 
endSentence = [".","!","...\n","?"]
midSentenceNoSpace = ["-"]
midWord = ["'"]
midSentenceSingleSpace = [",",":",";"]
mathNoSpace=["*","+","-","\\","^"]
mathSingleSpace=["%"]
openingBrackets = ["(","[","{","\""]
closingBrackets = [")","]","}","\""]

if not "-i" in sys.argv:
    print("requires an input file")
else:
    file_marker_index = sys.argv.index("-i")
    input_file_name = sys.argv[file_marker_index+1]
    with open(input_file_name, mode="r") as input:
        data = input.read()
    if "-o" in sys.argv:
        file_marker_index = sys.argv.index("-o")
        output_file_name = sys.argv[file_marker_index+1]
        
#create a vocabulary from the file
#note, in the case of several . in a row, it will treat all of the groupings of 3 as an ellipse, and the remaining . not in a grouping of 3 as a .
def split(data):
    result = {}  # For counting words and punctuation
    next_word = {}  # Nested dictionary to store frequency of words that follow each word
    brac= {}
    word = ""
    last_word = ""

    x = 0
    while x < len(data):
        char = data[x]
        if char.isalnum():  # If it's part of a word
            word += char.lower()
        else:
            if word:  # If there's a completed word
                if word in result:
                    result[word] += 1
                else:
                    result[word] = 1

                if last_word:  # Record transition
                    if last_word not in next_word:
                        next_word[last_word] = {}
                    if word in next_word[last_word]:
                        next_word[last_word][word] += 1
                    else:
                        next_word[last_word][word] = 1

                last_word = word  # Update last_word to the current word
                word = ""  # Reset word for the next one

            # Handle ellipses
            if char == "." and x + 2 < len(data) and data[x + 1:x + 3] == "..":
                ellipsis = "..."
                if x+3 == len(data) or (x+3<len(data) and data[x+3]=="\n"):
                    ellipsis = "...\n"
                if ellipsis in result:
                    result[ellipsis] += 1
                else:
                    result[ellipsis] = 1

                if last_word:  # Record transition from word to ellipsis
                    if last_word not in next_word:
                        next_word[last_word] = {}
                    if ellipsis in next_word[last_word]:
                        next_word[last_word][ellipsis] += 1
                    else:
                        next_word[last_word][ellipsis] = 1

                last_word = ellipsis  # Update last_word to ellipsis
                x += 3  # Skip the next two dots
                continue
            elif char not in [" ", "\n", "\t"]:  # Handle other punctuation
                if char in result:
                    result[char] += 1
                else:
                    result[char] = 1

                if last_word:  # Record transition from word to punctuation
                    if last_word not in next_word:
                        next_word[last_word] = {}
                    if char in next_word[last_word]:
                        next_word[last_word][char] += 1
                    else:
                        next_word[last_word][char] = 1

                last_word = char

            elif (char == "\n" and last_word in endSentence):  # Reset on newline
                last_word = ""
                word = ""

        x += 1

    # Handle any remaining word after the loop
    if word:
        if word in result:
            result[word] += 1
        else:
            result[word] = 1

        if last_word:
            if last_word not in next_word:
                next_word[last_word] = {}
            if word in next_word[last_word]:
                next_word[last_word][word] += 1
            else:
                next_word[last_word][word] = 1

    return result, next_word



wordCount, nextWord = split(data)
next_words = []
frequencies = []
mySentence = ""
chosenWord = ""
chosenWord=random.choice(list(wordCount.keys()))

while not chosenWord.isalnum():
    chosenWord=random.choice(list(wordCount.keys()))
mySentence+= chosenWord.capitalize()
prev = chosenWord
while not chosenWord in endSentence:
    next_word_freq_pairs = list(nextWord[prev].items())
    for key,val in nextWord[prev].items():
        next_words.append(key) 
        frequencies.append(val)
    # Select the next word based on its frequency
    chosenWord = random.choices(next_words, weights=frequencies, k=1)[0]
    if (chosenWord.isalnum() or chosenWord in openingBrackets) and prev not in midSentenceNoSpace and prev not in openingBrackets:
        mySentence += (" ")
    if chosenWord == "i":
        chosenWord = chosenWord.capitalize()
    mySentence+=chosenWord
    prev = chosenWord.lower() 
    next_words = []
    frequencies = []
print(mySentence)

