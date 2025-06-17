import random

endSentence = [".", "!", "...\n", "?"]
midSentenceNoSpace = ["-"]
midWord = ["'"]
midSentenceSingleSpace = [",", ":", ";"]
mathNoSpace = ["*", "+", "-", "\\", "^"]
mathSingleSpace = ["%"]
openingBrackets = ["(", "[", "{", "\""]
closingBrackets = [")", "]", "}", "\""]

def generate(data):
    wordCount, nextWord = split(data)
    next_words = []
    frequencies = []
    mySentence = ""
    chosenWord = random.choice(list(wordCount.keys()))

    while not chosenWord.isalnum():
        chosenWord = random.choice(list(wordCount.keys()))

    mySentence += chosenWord.capitalize()
    prev = chosenWord

    while not chosenWord in endSentence:
        next_word_freq_pairs = list(nextWord[prev].items())
        for key, val in nextWord[prev].items():
            next_words.append(key)
            frequencies.append(val)

        chosenWord = random.choices(next_words, weights=frequencies, k=1)[0]
        if (chosenWord.isalnum() or chosenWord in openingBrackets) and prev not in midSentenceNoSpace and prev not in openingBrackets:
            mySentence += (" ")
        if chosenWord == "i":
            chosenWord = chosenWord.capitalize()
        mySentence += chosenWord
        prev = chosenWord.lower()
        next_words = []
        frequencies = []

    return mySentence

def split(data):
    result = {}
    next_word = {}
    brac = {}
    word = ""
    last_word = ""

    x = 0
    while x < len(data):
        char = data[x]
        if char.isalnum():  
            word += char.lower()
        else:
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

                last_word = word  
                word = ""  

            if char == "." and x + 2 < len(data) and data[x + 1:x + 3] == "..":
                ellipsis = "..."
                if x + 3 == len(data) or (x + 3 < len(data) and data[x + 3] == "\n"):
                    ellipsis = "...\n"
                if ellipsis in result:
                    result[ellipsis] += 1
                else:
                    result[ellipsis] = 1

                if last_word:  
                    if last_word not in next_word:
                        next_word[last_word] = {}
                    if ellipsis in next_word[last_word]:
                        next_word[last_word][ellipsis] += 1
                    else:
                        next_word[last_word][ellipsis] = 1

                last_word = ellipsis  
                x += 3  
                continue
            elif char not in [" ", "\n", "\t"]:  
                if char in result:
                    result[char] += 1
                else:
                    result[char] = 1

                if last_word:  
                    if last_word not in next_word:
                        next_word[last_word] = {}
                    if char in next_word[last_word]:
                        next_word[last_word][char] += 1
                    else:
                        next_word[last_word][char] = 1

                last_word = char

            elif (char == "\n" and last_word in endSentence):  
                last_word = ""
                word = ""

        x += 1

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
